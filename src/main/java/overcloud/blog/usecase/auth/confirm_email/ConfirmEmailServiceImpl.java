package overcloud.blog.usecase.auth.confirm_email;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.infrastructure.auth.AuthError;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.auth.refresh_token.RefreshTokenRepository;

import java.util.Optional;

@Service
public class ConfirmEmailServiceImpl implements ConfirmEmailService{

    private final JpaRefreshTokenRepository refreshTokenRepository;

    private final IUserRepository userRepository;
    public ConfirmEmailServiceImpl(JpaRefreshTokenRepository refreshTokenRepository, IUserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public boolean confirmEmail(ConfirmEmailRequest confirmToken) {
        if (confirmToken == null || !StringUtils.hasText(confirmToken.getConfirmToken())) {
            throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
        }

        Optional<UserEntity> user = refreshTokenRepository.findUserByToken(confirmToken.getConfirmToken());

        if (user.isPresent()) {
            this.userRepository.enableUser(confirmToken.getConfirmToken());
        } else {
            throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
        }
        return true;
    }
}
