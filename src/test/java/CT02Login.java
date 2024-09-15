import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

public class CT02Login {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static Wait<WebDriver> espera;

    @BeforeEach
    public void setUp() {
        try {
            // Lê o arquivo JSON usando um BufferedReader
            buffer = new BufferedReader(new FileReader("src/main/resources/CT02Login.json"));
            json = new StringBuilder();
            while ((linha = buffer.readLine()) != null) {
                json.append(linha);
            }
            buffer.close();

            // Converte o conteúdo do arquivo JSON em um objeto JsonObject
            parser = new JsonParser();
            jsonObject = parser.parse(json.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Define as opções do Chrome
        options = new ChromeOptions();
        options.addArguments("start-maximized");

        // Inicializa o WebDriver
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);

        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));
    }

    @Test
    @DisplayName("CT02 - Login Usuário Inválido")
    public void CT02() {
        // Obtendo os dados do arquivo JSON
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();

        // Abrir a plataforma
        navegador.get(urlPlataforma);

        // Espera até o campo login aparecer
        espera.until(d -> navegador.findElement(By.name("login")));

        // Acha os campos e já preenche eles
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);

        // Clica no botão de login
        navegador.findElement(By.name("btn_entrar")).click();

        // Espera até pop up aparecer
        espera.until(d -> navegador.findElement(By.className("modal-title")));

        // Acha a mensagem de erro e coloca dentro de uma variável
        WebElement mensagemErro = navegador.findElement(By.className("modal-title"));

        // Verifica se a mensagem de erro recebida é a esperada
        Assertions.assertEquals("Erro", mensagemErro.getText());
    }
}