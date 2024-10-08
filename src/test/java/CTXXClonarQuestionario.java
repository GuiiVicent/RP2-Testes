import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
    @DisplayName("CTXX - Clonar Questionário")
    public void CTXX() throws InterruptedException {
        // Obtendo os dados do arquivo JSON
        String urlPlataforma = jsonObject.get("url").getAsString();
        String usuario = jsonObject.get("usuario").getAsString();
        String senha = jsonObject.get("senha").getAsString();
        String urlEsperada = jsonObject.get("urlEsperada").getAsString();

        // Abrir a plataforma
        navegador.get(urlPlataforma);

        // Espera até o campo login aparecer
        espera.until(d -> navegador.findElement(By.name("login")));

        // Acha os campos e já preenche eles
        navegador.findElement(By.name("login")).sendKeys(usuario);
        navegador.findElement(By.name("password")).sendKeys(senha);

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Clica no botão de login
        navegador.findElement(By.name("btn_entrar")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Compara se a url da página é a esperada
        Assertions.assertEquals(urlEsperada, navegador.getCurrentUrl());

        // Clica na aba de Questionário
        navegador.findElement(By.xpath("//*[@id=\"side-menu\"]/li[6]/a")).click();

        // metodo try
        lerArquivoJson("CTXXClonarQuestionario.json");
        String tituloQuestionario = jsonObject.get("tituloQuestionario").getAsString();

        // Espera até o campo de título estar visível na página
        espera.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Título']")));

        // Preenche o campo de título com o texto
        navegador.findElement(By.xpath("//input[@placeholder='Título']")).sendKeys(tituloQuestionario);

        // Clica no botão de pesquisar
        navegador.findElement(By.xpath("//*[@id=\"tbutton_find\"]")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Executa o clique usando JavaScript
        WebElement elemento = navegador.findElement(By.xpath("//th[text()='Título']"));
        ((JavascriptExecutor) navegador).executeScript("arguments[0].click();", elemento);

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Espera até o actions ser clicável usando o atributo class
        espera.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='btn btn-default btn-sm dropdown-toggle']")));

        // Clica no actions
        navegador.findElement(By.xpath("//button[@class='btn btn-default btn-sm dropdown-toggle']")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Espera até o clonar ser clicável
        espera.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[span='Clonar']")));

        // Clica no clonar
        navegador.findElement(By.xpath("//a[span='Clonar']")).click();

        // Espera um tempo determinado pra depois verificar
        Thread.sleep(timeSleep);

        // Verificando se chegou no modal title certo
        WebElement mensagemClonar = navegador.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/h4"));
        Assertions.assertEquals("Informação", mensagemClonar.getText());
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