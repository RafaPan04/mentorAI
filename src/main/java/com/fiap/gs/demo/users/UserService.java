package com.fiap.gs.demo.users;

import com.fiap.gs.demo.exceptions.BadRequestException;
import com.fiap.gs.demo.exceptions.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRankingService userRankingService;

    @Transactional
    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }

        if (userRepository.existsByCpf(createUserDTO.getCpf())) {
            throw new BadRequestException("CPF já cadastrado");
        }

        User user = User.builder()
                .nome(createUserDTO.getNome())
                .apelido(createUserDTO.getApelido())
                .email(createUserDTO.getEmail())
                .cpf(createUserDTO.getCpf())
                .idade(createUserDTO.getIdade())
                .build();

        User savedUser = userRepository.save(user);


        return UserResponseDTO.toDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + id));
        
        return UserResponseDTO.toDTO(user);
    }

    public List<UserRankingDTO> getRanking() {
        return userRankingService.getRanking();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuário não encontrado: " + id);
        }

        userRepository.deleteById(id);

    }
}

