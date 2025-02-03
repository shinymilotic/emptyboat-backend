package overcloud.blog.usecase.user.admin_create_user;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.response.ApiError;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;
import java.util.Optional;

@Service
public class AdminCreateUserImpl implements IAdminCreateUser {
    private final ObjectsValidator<AdminCreateUserRequest> validator;
    private final IUserRepository userRepository;
    private final SpringAuthenticationService authenticationService;

    public AdminCreateUserImpl(ObjectsValidator<AdminCreateUserRequest> validator, IUserRepository userRepository, SpringAuthenticationService authenticationService) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Void adminCreateUser(AdminCreateUserRequest request) {
        Optional<ApiError> error = validator.validate(request);

        if (error.isPresent()) {
            throw new InvalidDataException(error.get());
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            error = validator.addError(error, UserResMsg.USER_EMAIL_EXIST);
        }

        if (userRepository.findByUsername(request.getUsername()) != null) {
            error = validator.addError(error, UserResMsg.USER_USERNAME_EXIST);
        }

        if (error.isPresent()) {
            throw new InvalidDataException(error.get());
        }

        if (!StringUtils.hasText(request.getImage())) {
            request.setImage("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png");
        }

        String hashedPassword = authenticationService.encodePassword(request.getPassword());

        UserEntity userForSave = new UserEntity();
        userForSave.setUserId(UuidCreator.getTimeOrderedEpoch());
        userForSave.setUsername(request.getUsername());
        userForSave.setPassword(hashedPassword);
        userForSave.setEmail(request.getEmail());
        userForSave.setBio(request.getBio());
        userForSave.setImage(request.getImage());
        userForSave.setEnable(request.isEnabled());
        userRepository.save(userForSave);

        return null;
    }
}
