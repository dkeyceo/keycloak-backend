package com.dkey.keycloakbackend.controller;

import com.dkey.keycloakbackend.dto.ResponseMessage;
import com.dkey.keycloakbackend.model.Foo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/foo")
@CrossOrigin
public class FooController {
    private List<Foo> foos =
            Stream.of(new Foo(1, "foo1"),
                    new Foo(2, "foo2"),
                    new Foo(3, "foo3"))
                    .collect(Collectors.toList());

    @GetMapping("/list")
    @RolesAllowed("backend-user")
    public ResponseEntity<List<Foo>> list(){
        return ResponseEntity.status(HttpStatus.OK).body(foos);
    }
    
    @GetMapping("/detail/{id}")
    @RolesAllowed("backend-user")
    public ResponseEntity<Foo> detail(@PathVariable int id){
        Foo foo = foos
                .stream().filter(f -> f.getId() == id).findFirst().orElse(null);

        return ResponseEntity.status(200).body(foo);
    }
    @PostMapping("/create")
    @RolesAllowed("backend-admin")
    public ResponseEntity<?> create(@RequestBody Foo foo){
        int maxIndex = foos.stream().max(Comparator.comparing(m->m.getId())).get().getId();
        foo.setId(maxIndex + 1);
        foos.add(foo);
        return ResponseEntity.status(201).body(new ResponseMessage("Created"));
    }
    @PutMapping("/update/{id}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<?> update(@PathVariable int id,@RequestBody Foo foo){
        Foo fooUpdate = foos.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
        fooUpdate.setName(foo.getName());
        foos.add(fooUpdate);
        return ResponseEntity.status(200).body(new ResponseMessage("updated"));
    }
    @DeleteMapping("/delete/{id}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<?> delete(@PathVariable int id){
        Foo foo = foos.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
        foos.remove(foo);
        return ResponseEntity.status(200).body(new ResponseMessage("deleted"));
    }
}
