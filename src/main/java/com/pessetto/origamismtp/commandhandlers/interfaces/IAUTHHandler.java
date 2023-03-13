/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pessetto.origamismtp.commandhandlers.interfaces;

import com.pessetto.origamismtp.status.AuthStatus;

/**
 *
 * @author Travis pessetto
 */
public interface IAUTHHandler {
    public String getResponse(String cmd);
    public AuthStatus getStatus();
}
