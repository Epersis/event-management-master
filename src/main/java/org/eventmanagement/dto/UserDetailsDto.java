package org.eventmanagement.dto;

import java.io.Serializable;

import org.eventmanagement.enums.Role;

public class UserDetailsDto implements Serializable {

    private long id;

    private String firstName;

    private String lastName;

    private long mobileNumber;

    private String email;

    private Role role;

    private WalletDto walletDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public WalletDto getWalletDetails() {
        return walletDetails;
    }

    public void setWalletDetails(WalletDto walletDetails) {
        this.walletDetails = walletDetails;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
