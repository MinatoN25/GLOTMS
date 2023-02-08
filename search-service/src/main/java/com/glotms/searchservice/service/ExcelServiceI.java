package com.glotms.searchservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.glotms.searchservice.model.Ticket;

public interface ExcelServiceI {
	ByteArrayInputStream load(List<Ticket> tickets) throws IOException;
}
