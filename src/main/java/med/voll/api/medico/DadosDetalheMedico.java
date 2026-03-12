package med.voll.api.medico;

public record DadosDetalheMedico(Long id, String nome) {
    public DadosDetalheMedico (Medico medico){
        this(medico.getId(), medico.getNome());
    }
}
