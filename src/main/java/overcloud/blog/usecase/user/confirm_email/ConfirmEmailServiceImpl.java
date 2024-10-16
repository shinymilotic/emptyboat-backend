package overcloud.blog.usecase.user.confirm_email;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.response.ResFactory;
import overcloud.blog.response.RestResponse;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.user.common.UserResMsg;

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
            throw new InvalidDataException(resFactory.fail(UserResMsg.AUTHORIZE_FAILED));
        }

        Optional<UserEntity> user = refreshTokenRepository.findUserByToken(confirmToken.getConfirmToken());

        if (user.isPresent()) {
            this.userRepository.enableUser(confirmToken.getConfirmToken());
        } else {
            throw new InvalidDataException(resFactory.fail(UserResMsg.AUTHORIZE_FAILED));
        }

        return resFactory.success(UserResMsg.AUTHORIZE_SUCCESS, null);
    }
}
