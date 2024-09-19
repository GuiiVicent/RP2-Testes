import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

import static java.lang.Thread.sleep;

public class CTXXClonarQuestionario {
    BufferedReader buffer;
    StringBuilder json;
    String linha;
    JsonParser parser;
    JsonObject jsonObject;
    ChromeOptions options;
    static WebDriver navegador;
    static Wait<WebDriver> espera;
    int timeSleep = 2000;

    @BeforeEach
    public void setUp() {
        // metodo try
        lerArquivoJson("CT01LoginDadosCorretos.json");

        // Define as opções do Chrome
        options = new ChromeOptions();
        options.addArguments("start-maximized");

        // Inicializa o WebDriver
        WebDriverManager.chromedriver().setup();
        navegador = new ChromeDriver(options);

        espera = new WebDriverWait(navegador, Duration.ofSeconds(50));
    }

    @Test
    @DisplayName("CT01 - Login Usuário Válido")
    public void CT01() throws InterruptedException {
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

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Compara se a url da página é a esperada
        Assertions.assertEquals("http://200.132.136.72/AIQuiz/index.php?class=EmptyPage&previous_class=LoginForm", navegador.getCurrentUrl());

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Clica na aba de Questionário
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[5]/a/i")).click();

        // metodo try
        lerArquivoJson("CTXXClonarQuestionario.json");
        String tituloQuestionario = jsonObject.get("tituloQuestionario").getAsString();

        // Espera até o campo título aparecer
        espera.until(d -> navegador.findElement(By.name("titulo")));

        // Preenche o campo de titulo com o titulo do questionario desejado
        navegador.findElement(By.name("titulo")).sendKeys(tituloQuestionario);

        // Declara um objeto de actions
        Actions actions = new Actions(navegador);

        // Pressiona Enter para pesquisar o questionário desejado
        actions.sendKeys(Keys.ENTER).perform();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Clica no botão actions
        navegador.findElement(By.xpath("//*[@id=\"olá_mundo\"]/tbody/tr[1]/td[4]/div/button")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Clica no botão de clonar
        navegador.findElement(By.xpath("//*[@id=\"olá_mundo\"]/tbody/tr[1]/td[4]/div/ul/li[4]/a")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Verificando se chegou no modal title certo
        WebElement mensagemErro = navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/span[2]"));
        Assertions.assertEquals("Registro salvo", mensagemErro.getText());
    }

    // metodo Try para ler o arquivo .json com um BufferedReader
    public void lerArquivoJson(String jsonArquivo){
        try {
            buffer = new BufferedReader(new FileReader("src/main/resources/" + jsonArquivo));
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
    }
}