package com.bernatskiy.springtestbot.service.impl;

import com.bernatskiy.springtestbot.entity.Procedure;
import com.bernatskiy.springtestbot.service.ProcedureModeService;

import java.util.HashMap;
import java.util.Map;

public class HashMapProcedureModeService implements ProcedureModeService {

    private final Map<Long, Procedure> procedure = new HashMap<>();

    public HashMapProcedureModeService() {
        System.out.println("HASHMAP MODE is created");
    }

    @Override
    public Procedure getProcedure(long chatId) {
        return procedure.getOrDefault(chatId, Procedure.ЧИСТКА);
    }

    @Override
    public void setProcedure(long chatId, Procedure procedureType) {
        procedure.put(chatId, procedureType);
    }
}