package org.irfan.library.services;

import org.irfan.library.Model.Author;
import org.irfan.library.dao.AuthorRepository;
import org.irfan.library.dto.GetAuthorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;
    public List<GetAuthorDTO> getAllAuthors()
    {
        List<GetAuthorDTO> authorsDTO = new ArrayList<>();
        List<Author> authors = authorRepository.findAll();
        for(Author author : authors){
            authorsDTO.add(new GetAuthorDTO(author.getName()));
        }
        return authorsDTO;
    }

    public GetAuthorDTO getAuthorByName(String name){
        Author author = authorRepository.findByName(name);
        return new GetAuthorDTO(author.getName());
    }
}
