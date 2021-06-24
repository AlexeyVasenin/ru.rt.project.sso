package ru.rt.test.test_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class TestController {

    private final ModelRepository modelRepository;

    @Autowired
    public TestController(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @GetMapping("/models")
    public String getModelsCount() {
        return String.format("Model count: %d", modelRepository.findAll().size());
    }
}
