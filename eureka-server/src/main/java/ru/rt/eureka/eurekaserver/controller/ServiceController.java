package ru.rt.eureka.eurekaserver.controller;

import com.netflix.discovery.shared.Application;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    @GetMapping
    public ResponseEntity<List<Application>> getAllRegisteredApplicationsBesidesResourceServer() {
        return new ResponseEntity<>(listAllRegisteredApplicationsBesidesResourceServer(), HttpStatus.OK);
    }

    @GetMapping("/urls")
    public List<String> getAllRegisteredApplicationsURLsBesidesResourceServer() {
        List<Application> registeredApplications = listAllRegisteredApplicationsBesidesResourceServer();
        List<String> URLs = new ArrayList<>();

        // берем statusPageURL, например, http://localhost:8083/music/actuator/info и забираем только http://localhost:8083/music
        registeredApplications.forEach(x -> x.getInstances().forEach(instance -> {
            String s = instance.getStatusPageUrl().replace("/actuator/info", "");
            URLs.add(s);
        }));

        return URLs;
    }

    private List<Application> listAllRegisteredApplicationsBesidesResourceServer() {
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        List<Application> registeredApplications = registry.getApplications().getRegisteredApplications();

        registeredApplications.remove(registeredApplications
                .stream()
                .filter(x -> x.getName().equals("RESOURCE-SERVER"))
                .findFirst()
                .orElse(null));

        return registeredApplications;
    }
}
