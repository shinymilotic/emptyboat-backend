package overcloud.blog.usecase.user.confirm_email;

import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.common.auth.AuthResMsg;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.ResFactory;
import overcloud.blog.usecase.common.response.RestResponse;
import overcloud.blog.usecase.user.refresh_token.RefreshTokenRepository;

import java.util.Optional;

@Service
public class ConfirmEmailServiceImpl implements ConfirmEmailService{
    private final JpaRefreshTokenRepository refreshTokenRepository;
    private final ResFactory resFactory;
    private final IUserRepository userRepository;

    public ConfirmEmailServiceImpl(JpaRefreshTokenRepository refreshTokenRepository,
                                    IUserRepository userRepository,
                                    ResFactory resFactory) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.resFactory = resFactory;
    }

    @Override
    @Transactional
    public RestResponse<Void> confirmEmail(ConfirmEmailRequest confirmToken) {
        if (confirmToken == null || !StringUtils.hasText(confirmToken.getConfirmToken())) {
            throw new InvalidDataException(resFactory.fail(AuthResMsg.AUTHORIZE_FAILED));
        }

        Optional<UserEntity> user = refreshTokenRepository.findUserByToken(confirmToken.getConfirmToken());

        if (user.isPresent()) {
            this.userRepository.enableUser(confirmToken.getConfirmToken());
        } else {
            throw new InvalidDataException(resFactory.fail(AuthResMsg.AUTHORIZE_FAILED));
        }
        
        return resFactory.success(AuthResMsg.AUTHORIZE_SUCCESS, null);
    }
}
