package com.example.core_system_rbac.services;

import com.example.core_system_rbac.dtos.request.ChangePasswordRequest;
import com.example.core_system_rbac.dtos.request.UserCreateRequest;
// Giả định bạn có UserUpdateRequest (thường không có password)
import com.example.core_system_rbac.dtos.response.UserResponse;
import com.example.core_system_rbac.entities.Role;
import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.entities.UserRole;
import com.example.core_system_rbac.exceptions.DuplicateResourceException;
import com.example.core_system_rbac.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.core_system_rbac.repositories.IRoleRepository;
import com.example.core_system_rbac.repositories.IUserRepository;
import com.example.core_system_rbac.repositories.IUserRoleRepository;
// Tùy chỉnh mapper của bạn
import com.example.core_system_rbac.mappers.UserMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    IUserRepository userRepository;
    IRoleRepository roleRepository;
    IUserRoleRepository userRoleRepository;

    UserMapper userMapper;

    // Thư viện mã hóa của Spring Security
    PasswordEncoder passwordEncoder;

    // ==========================================
    // 1. TẠO MỚI USER (Mã hóa BCrypt & Mặc định STAFF)
    // ==========================================
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        // Kiểm tra email trùng lặp (truyền null vì tạo mới không có id để loại trừ)
        validateEmailUniqueness(request.getEmail(), null);

        // Map DTO sang Entity
        User user = userMapper.toEntity(request);

        // MÃ HÓA BCRYPT: Ghi đè password dạng thô bằng chuỗi đã băm
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Thiết lập trạng thái ban đầu
        user.setIsActive(request.getIsActive());
        user.setIsDeleted(false);

        // XỬ LÝ QUY TẮC MẶC ĐỊNH LÀ "STAFF"
        Set<UserRole> userRoles = new HashSet<>();
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            Role defaultRole = roleRepository.findByRoleCode("STAFF") // Cần thêm hàm này trong IRoleRepository
                    .orElseThrow(() -> new ResourceNotFoundException("Hệ thống chưa được cấu hình quyền STAFF mặc định"));

            userRoles.add(UserRole.builder().user(user).role(defaultRole).build());
        } else {
            // Nếu có truyền roleIds, map từng role
            for (UUID roleId : request.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role ID không tồn tại: " + roleId));
                userRoles.add(UserRole.builder().user(user).role(role).build());
            }
        }

        user.setUserRoles(userRoles);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    // ==========================================
    // 2. LẤY DỮ LIỆU
    // ==========================================
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllActiveUsers(Pageable pageable) {
        Page<User> users = userRepository.findByIsDeletedFalse(pageable);
        return users.map(userMapper::toResponse);
    }

    // ==========================================
    // 3. XÓA MỀM (2 CHẾ ĐỘ & BẢO VỆ IS_SYSTEM)
    // ==========================================
    @Transactional
    public void deleteUser(UUID id, boolean isDeepDelete) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));

        // QUY TẮC BẤT KHẢ XÂM PHẠM (is_system)
        // Không cho phép xóa User đang nắm giữ quyền hệ thống (VD: Super Admin)
        boolean hasSystemRole = user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getIsSystem());

        if (hasSystemRole) {
            throw new IllegalArgumentException("Bất khả xâm phạm: Không thể xóa tài khoản đang nắm giữ quyền System!");
        }

        if (isDeepDelete) {
            // CHẾ ĐỘ XÓA VÉT CẠN: Xóa vĩnh viễn các liên kết trong bảng user_role
            // Lưu ý: Chỉ xóa liên kết, bản ghi User vẫn là xóa mềm
            userRoleRepository.deleteByUserId(user.getId());
        }
        // Nếu !isDeepDelete -> Giữ nguyên các liên kết user_role để sau này có thể Restore

        // Đánh dấu xóa mềm User
        userRepository.softDelete(id);
    }

    // ==========================================
    // 4. KHÔI PHỤC TÀI KHOẢN
    // ==========================================
    @Transactional
    public void restoreUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));

        userRepository.restore(user.getId());
    }

    // ==========================================
    // LẤY THÔNG TIN THEO EMAIL (Dùng cho quá trình Login)
    // ==========================================
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với email: " + email));

        return userMapper.toResponse(user);
    }

    // ==========================================
    // CÁC HÀM VALIDATE DÙNG CHUNG
    // ==========================================
    private void validateEmailUniqueness(String email, UUID excludeUserId) {
        userRepository.findByEmailAndIsDeletedFalse(email).ifPresent(existingUser -> {
            // Nếu tìm thấy email, nhưng id trùng với id đang update thì bỏ qua
            if (excludeUserId == null || !existingUser.getId().equals(excludeUserId)) {
                throw new DuplicateResourceException("Email " + email + " đã được sử dụng!");
            }
        });
    }

    // ==========================================
    // 5. THAY ĐỔI MẬT KHẨU
    // ==========================================
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng!"));

        // 1. Kiểm tra mật khẩu cũ có khớp với DB hay không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác!");
        }

        // 2. Kiểm tra mật khẩu mới và mật khẩu xác nhận có khớp nhau không
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp với mật khẩu mới!");
        }

        // 3. (Tùy chọn) Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới không được giống mật khẩu hiện tại!");
        }

        // 4. Băm mật khẩu mới và lưu lại
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}