public class Aluno {

    private String nome;
    private int ra;
    private int idade;
    private String sexo;
    private double media;
    private String resultado;

    public Aluno(String nome, int ra, int idade, String sexo, double media) {
        this.nome = nome;
        this.ra = ra;
        this.idade = idade;
        this.sexo = sexo;
        this.media = media;
        this.resultado = calcularResultado(media);
    }

    /**
     * Regra de negócio: Média >= 6.0 => "Aprovado", caso contrário "Reprovado".
     */
    public static String calcularResultado(double media) {
        return (media >= 6.0) ? "Aprovado" : "Reprovado";
    }

    // ---------- Getters e Setters ----------

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRa() {
        return ra;
    }

    public void setRa(int ra) {
        this.ra = ra;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
        this.resultado = calcularResultado(media); // mantém o resultado sempre coerente
    }

    public String getResultado() {
        return resultado;
    }

    /**
     * Formata a exibição de um aluno em forma de tabela/linha.
     */
    @Override
    public String toString() {
        return String.format("%-25s | RA: %-8d | Idade: %-3d | Sexo: %-6s | Média: %-5.2f | %s",
                nome, ra, idade, sexo, media, resultado);
    }
}