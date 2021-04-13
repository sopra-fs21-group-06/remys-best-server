package ch.uzh.ifi.hase.soprafs21.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles authentication / registration dance
 */
@Service
@Transactional
public class LoginService {

    Logger log = LoggerFactory.getLogger(LoginService.class);


}
