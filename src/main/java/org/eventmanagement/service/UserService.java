package org.eventmanagement.service;

import java.util.Optional;

import org.eventmanagement.converter.ObjectConverter;
import org.eventmanagement.dto.UserDetailsDto;
import org.eventmanagement.dto.UserDetailsImpl;
import org.eventmanagement.dto.WalletDto;
import org.eventmanagement.enums.Role;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.model.User;
import org.eventmanagement.model.Wallet;
import org.eventmanagement.repository.UserRepository;
import org.eventmanagement.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ObjectConverter objectConverter;

    public UserDetailsDto getUserProfile() throws EntityDoesNotExistException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String whoAmI = "";
        Role role = Role.ROLE_CUSTOMER;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            whoAmI = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
            role = ((UserDetailsImpl) authentication.getPrincipal()).getRole();
        }

        User user = this.userRepository.findByEmail(whoAmI).orElseThrow(() -> new EntityDoesNotExistException("User " +
                "does " +
                "not exist")); 
        UserDetailsDto userDetailsDto = (UserDetailsDto) this.objectConverter.convert(user, UserDetailsDto.class);

        Optional<Wallet> wallet = this.walletRepository.findByUserId(user.getId());

        if (wallet.isPresent()) {
            WalletDto walletDto = (WalletDto) this.objectConverter.convert(wallet.get(), WalletDto.class);
            userDetailsDto.setWalletDetails(walletDto);
        }
        return userDetailsDto;
    }


}
