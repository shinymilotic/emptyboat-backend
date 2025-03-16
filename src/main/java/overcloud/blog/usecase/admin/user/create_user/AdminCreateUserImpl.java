package overcloud.blog.usecase.admin.user.create_user;

import com.github.f4b6a3.uuid.UuidCreator;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import overcloud.blog.auth.service.SpringAuthenticationService;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.response.ApiError;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AdminCreateUserImpl implements AdminCreateUser {
    private final ObjectsValidator<AdminCreateUserRequest> validator;
    private final UserRepository userRepository;
    private final SpringAuthenticationService authenticationService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminCreateUserImpl(ObjectsValidator<AdminCreateUserRequest> validator, UserRepository userRepository, SpringAuthenticationService authenticationService) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Void adminCreateUser(AdminCreateUserRequest request) throws IOException {
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
        userForSave.setImage(this.saveImage(request.getImage()));
        userForSave.setEnable(request.isEnabled());
        userRepository.save(userForSave);

        return null;
    }

    private String saveImage(String base64Image) throws IOException {
        byte[] data = Base64.decodeBase64(base64Image);
        String fileType = getImageType(data);
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = this.getFileName(fileType);
        Path filePath = uploadPath.resolve(fileName);
        try (OutputStream stream = new FileOutputStream(filePath.toString())) {
            stream.write(data);
        }

        return "http://localhost:8081/api/images/" + fileName;
    }

    private String getFileName(String fileType) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(UuidCreator.getTimeOrderedEpoch().toString());
        fileName.append(".");
        fileName.append(fileType);

        return fileName.toString();
    }

    private boolean isMatch(byte[] pattern, byte[] data) {
        if (pattern.length <= data.length) {
            for (int idx = 0; idx < pattern.length; ++idx) {
                if (pattern[idx] != data[idx])
                    return false;
            }
            return true;
        }

        return false;
    }

    private String getImageType(byte[] data) {
//        filetype    magic number(hex)
//        jpg         FF D8 FF
//        gif         47 49 46 38
//        png         89 50 4E 47 0D 0A 1A 0A
//        bmp         42 4D
//        tiff(LE)    49 49 2A 00
//        tiff(BE)    4D 4D 00 2A

        final byte[] pngPattern = new byte[] { (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        final byte[] jpgPattern = new byte[] { (byte)0xFF, (byte)0xD8, (byte)0xFF};
        final byte[] gifPattern = new byte[] { 0x47, 0x49, 0x46, 0x38};
        final byte[] bmpPattern = new byte[] { 0x42, 0x4D };
        final byte[] tiffLEPattern = new byte[] { 0x49, 0x49, 0x2A, 0x00};
        final byte[] tiffBEPattern = new byte[] { 0x4D, 0x4D, 0x00, 0x2A};
        if (isMatch(pngPattern, data))
            return "png";

        if (isMatch(jpgPattern, data))
            return "jpg";

        if (isMatch(gifPattern, data))
            return "gif";

        if (isMatch(bmpPattern, data))
            return "bmp";

        if (isMatch(tiffLEPattern, data))
            return "tif";

        if (isMatch(tiffBEPattern, data))
            return "tif";

        return "png";
    }
}
