package org.eventmanagement.dto;

import java.io.Serializable;

public class WalletDto implements Serializable {

    private int walletId;

    private long balance;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
