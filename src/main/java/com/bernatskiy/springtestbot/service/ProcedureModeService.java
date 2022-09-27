package com.bernatskiy.springtestbot.service;

import com.bernatskiy.springtestbot.entity.Procedure;
import com.bernatskiy.springtestbot.service.impl.HashMapProcedureModeService;

public interface ProcedureModeService {

    static ProcedureModeService getInstance() {
        return new HashMapProcedureModeService();
    }

    Procedure getProcedure(long chatId);

    void setProcedure(long chatId, Procedure procedure);
}
