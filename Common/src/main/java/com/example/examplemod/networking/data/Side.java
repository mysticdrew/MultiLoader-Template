package com.example.examplemod.networking.data;

public enum Side
{
    CLIENT,
    SERVER;

    public Side opposite()
    {
        if (CLIENT.equals(this))
        {
            return SERVER;
        }
        return CLIENT;
    }
}
