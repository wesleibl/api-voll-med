package med.voll.api.medico;

public record DadosPosCadastro(Long id, String nome) {
    public DadosPosCadastro (Medico medico){
        this(medico.getId(), medico.getNome());
    }
}
