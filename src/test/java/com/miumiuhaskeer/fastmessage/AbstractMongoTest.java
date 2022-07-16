package com.miumiuhaskeer.fastmessage;

import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.service.UserService;
import com.miumiuhaskeer.fastmessage.util.JWTokenUtil;
import com.miumiuhaskeer.fastmessage.util.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoConfigureDataMongo
public abstract class AbstractMongoTest extends AbstractTest {

    @Autowired
    protected TestUtils testUtils;

    protected static ExtendedUserDetails admin;
    protected static String adminHeader;

    protected static ExtendedUserDetails user1;
    protected static String user1Header;

    protected static ExtendedUserDetails user2;
    protected static String user2Header;

    @BeforeAll
    public static void initUser(@Autowired UserService userService, @Autowired JWTokenUtil jwTokenUtil) {
        String adminEmail = "admin@mail.ru";
        String user1Email = "user1@mail.ru";
        String user2Email = "user2@mail.ru";
        String password = "12345678a";

        admin = userService.authenticate(adminEmail, password);
        adminHeader = jwTokenUtil.generateHeader(jwTokenUtil.generateToken(admin));

        user1 = userService.authenticate(user1Email, password);
        user1Header = jwTokenUtil.generateHeader(jwTokenUtil.generateToken(user1));

        user2 = userService.authenticate(user2Email, password);
        user2Header = jwTokenUtil.generateHeader(jwTokenUtil.generateToken(user2));

        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    public void clearMongoDB() {
        testUtils.clearAllFromMongo();
    }
}
