package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.repository.ViewRepository;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import jakarta.validation.constraints.NotBlank;

@Validated
@Service
public class TripTypeServiceImpl implements TripTypeService {

	@Autowired
	private ViewRepository viewRepository;

	@Override
	public List<TripType> findAll() {
		return viewRepository.findAll();
	}

	@Override
	public TripType getAllById(@NotBlank Long id) {
		return viewRepository.findAllById(id);
	}

	@Override
	public TripType getById(@NotBlank Long id) {
		return viewRepository.findById(id);
	}
}
