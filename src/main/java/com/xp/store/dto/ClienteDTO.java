package com.xp.store.dto;

import com.xp.store.model.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Valid
public record ClienteDTO(

        UUID id,

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        String cpf,

        String telefone,

        String dataNascimento,

        String endereco,

        String cidade,

        String estado,

        String cep

) {
    public Cliente convertUser(UUID id) {
        Cliente user = new Cliente();
        user.setId(id);
        user.setNome(name);
        user.setEmail(email);
        user.setSenha(senha);
        user.setCpf(cpf);
        user.setTelefone(telefone);
        user.setDataNascimento(dataNascimento);
        user.setEndereco(endereco);
        user.setCidade(cidade);
        user.setEstado(estado);
        user.setCep(cep);
        return user;
    }
}