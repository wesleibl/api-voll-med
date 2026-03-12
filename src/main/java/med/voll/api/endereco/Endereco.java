package med.voll.api.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    private String logradouro;
    private String bairro;
    private String cep;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(DadosEndereco endereco) {
        this.uf = endereco.uf();
        this.logradouro = endereco.logradouro();
        this.cep = endereco.cep();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.complemento = endereco.complemento();
    }

    public void atualizarInformacoes(DadosEndereco dados) {
        if(dados.logradouro() != null){
            this.logradouro = dados.logradouro();
        }
        if(dados.uf() != null){
            this.uf = dados.uf();
        }
        if(dados.cep() != null){
            this.cep = dados.cep();
        }
        if(dados.bairro() != null){
            this.bairro = dados.bairro();
        }
        if(dados.cidade() != null){
            this.cidade = dados.cidade();
        }
        if(dados.complemento() != null){
            this.complemento = dados.complemento();
        }
    }
}
