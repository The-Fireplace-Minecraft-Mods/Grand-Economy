package com.kamildanak.minecraft.enderpay.commands;

import net.minecraft.command.CommandException;

class InsufficientCreditException extends CommandException {

    InsufficientCreditException() {
        //noinspection RedundantArrayCreation
        this("You don't have money you want to pay :(.", new Object[0]);
    }

    private InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}