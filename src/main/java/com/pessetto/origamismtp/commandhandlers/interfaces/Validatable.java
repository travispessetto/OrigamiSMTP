package com.pessetto.origamismtp.commandhandlers.interfaces;

public interface Validatable 
{
	String getResponse();
	Validatable validateOrNullify();
}
