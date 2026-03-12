package med.voll.api.medico;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicoControllerTest {

    private static final String NOVOMEDICO = """
                {
                    "nome": "Medico Teste",
                    "email": "medico@teste.com",
                    "telefone": "61999999999",
                    "crm": "123456",
                    "especialidade": "CARDIOLOGIA",
                    "endereco": {
                        "logradouro": "rua 1",
                        "bairro": "bairro",
                        "cep": "00000000",
                        "cidade": "brasilia",
                        "uf": "DF",
                        "complemento": "apto"
                    }
                }
                """;
    @Autowired
    private MedicoRepository repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void deveRetornarMedicos() throws Exception {
        var dados = objectMapper.readValue(NOVOMEDICO, DadosCadastroMedico.class);

        repository.save(new Medico(dados));

        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Medico Teste"));
    }

    @Test
    @Transactional
    void deveRetornarIdENome() throws Exception {
        mockMvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(NOVOMEDICO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Medico Teste"));
    }

    @Test
    @Transactional
    void deveAtualizarMedico() throws Exception {
        var dados = objectMapper.readValue(NOVOMEDICO, DadosCadastroMedico.class);
        var medicoSalvo = repository.save(new Medico(dados));

        String novoNome = "Nome Atualizado e Correto";
        var jsonUpdate = """
                {
                    "id": %d,
                    "nome": "%s"
                }
                """.formatted(medicoSalvo.getId(), novoNome);

        mockMvc.perform(put("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(medicoSalvo.getId()))
                .andExpect(jsonPath("$.nome").value(novoNome));
    }

    @Test
    @Transactional
    void deveExcluirMedico() throws Exception{
        var dados = objectMapper.readValue(NOVOMEDICO, DadosCadastroMedico.class);
        var medicoSalvo = repository.save(new Medico(dados));

        mockMvc.perform(delete("/medicos/"+ medicoSalvo.getId()))
                .andExpect(status().isNoContent());

        em.flush();
        em.clear();

        var medicoNoBanco = repository.getReferenceById(medicoSalvo.getId());

        Assertions.assertFalse(medicoNoBanco.getAtivo());
    }
}
