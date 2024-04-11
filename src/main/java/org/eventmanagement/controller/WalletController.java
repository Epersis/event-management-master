
package org.eventmanagement.controller;

import java.util.Optional;

import org.eventmanagement.dto.WalletDto;
import org.eventmanagement.exception.BadRequestException;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PutMapping("/{walletId}/add/{amount}")
    @CrossOrigin
    public ResponseEntity<?> addMoneyToWallet(@PathVariable Long walletId, @PathVariable long amount)
            throws BadRequestException,
            EntityDoesNotExistException {

        WalletDto savedWallet = this.walletService.addMoneyToWallet(walletId, amount);
        return new ResponseEntity<>(savedWallet, HttpStatus.OK);
    }

}
