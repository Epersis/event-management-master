package org.eventmanagement.service;

import org.eventmanagement.converter.ObjectConverter;
import org.eventmanagement.dto.WalletDto;
import org.eventmanagement.exception.EntityDoesNotExistException;
import org.eventmanagement.model.Wallet;
import org.eventmanagement.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ObjectConverter objectConverter;
    public WalletDto addMoneyToWallet(Long walletId, Long balance) throws EntityDoesNotExistException{
        Wallet wallet = this.walletRepository.findById(walletId).orElseThrow(() -> 
        new EntityDoesNotExistException("Wallet does not exist"));
        wallet.setBalance(wallet.getBalance() +balance);
       Wallet updatedWallet=this.walletRepository.save(wallet);
         return (WalletDto) this.objectConverter.convert(updatedWallet, WalletDto.class);
    }

}
