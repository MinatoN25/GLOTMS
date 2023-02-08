package com.glotms.searchservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.glotms.searchservice.model.Ticket;
import com.glotms.searchservice.utils.ExcelHelper;

@Service
public class ExcelService implements ExcelServiceI {

	@Override
	public ByteArrayInputStream load(List<Ticket> tickets) throws IOException {
		System.out.println(tickets);
		ByteArrayInputStream in = ExcelHelper.ticketsToExcel(tickets);
		return in;
	}

}