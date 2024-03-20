package org.irfan.library.services;

import org.irfan.library.dto.AuthorDTO;
import org.irfan.library.dto.request.CreateAuthorRequest;
import org.irfan.library.dto.request.EditAuthorRequest;

import java.util.List;
import java.util.Optional;

public interface AuthorService{
    List<AuthorDTO> getAuthorsByCriteria(Optional<Integer> id, Optional<String> firstname, Optional<String> lastname, Optional<Integer> bookId);
    AuthorDTO getAuthorById(Integer id);
    AuthorDTO createAuthor(CreateAuthorRequest request);
    AuthorDTO editAuthor(Integer id, EditAuthorRequest request);
    void deleteAuthor(Integer id);
}
