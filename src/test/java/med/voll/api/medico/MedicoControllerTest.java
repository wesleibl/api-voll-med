package med.voll.api.medico;

import med.voll.api.endereco.DadosEndereco;
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

@SpringBootTest
@AutoConfigureMockMvc
public class MedicoControllerTest {

    @Autowired
    private MedicoRepository repository;

    @Autowired
    private MockMvc mockMvc;

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null
        );
    }

    @Test
    @Transactional
    void deveRetornarMedicos() throws Exception {
        var dados = new DadosCadastroMedico("João", "joao@voll.med", "123456",Especialidade.ORTOPEDIA, this.dadosEndereco(), "59995293322");
        repository.save(new Medico(dados));

        mockMvc.perform(get("/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("João"));
    }

    @Test
    @Transactional
    void cadastrarMedico() throws Exception {
        var jsonInput = """
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
                        "numero": "1",
                        "complemento": "apto"
                    }
                }
                """;

        mockMvc.perform(post("/medicos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Medico Teste"));
    }
}
