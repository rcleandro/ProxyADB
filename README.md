# ProxyADB 🚀

**ProxyADB** é uma ferramenta desktop desenvolvida com **Compose Multiplatform** para facilitar a configuração de proxy HTTP em dispositivos Android via ADB. 

O objetivo principal é automatizar o processo de habilitar e desabilitar o proxy no dispositivo, detectando automaticamente o IP da máquina local e permitindo a alteração da porta, tudo através de uma interface moderna e intuitiva.

## ✨ Funcionalidades

- **Detecção Automática de IP**: Identifica o endereço IPv4 da sua rede local (Wi-Fi/Ethernet) automaticamente.
- **Toggle One-Click**: Ative ou desative o proxy no dispositivo Android conectado com apenas um clique.
- **Configuração de Porta**: Personalize a porta do proxy (padrão: 8888).
- **Status do ADB**: Monitoramento em tempo real da disponibilidade do ADB no sistema.
- **Preview de Comando**: Visualize o comando `adb shell` exato que será executado.
- **Suporte Multi-idioma**: Disponível em Português, Inglês e Espanhol.
- **Interface Dark Mode**: Design moderno inspirado em ferramentas de desenvolvedor.

## 🛠️ Tecnologias Utilizadas

- **Kotlin Multiplatform**: Estrutura do projeto.
- **Compose Multiplatform**: Interface de usuário (Desktop JVM).
- **Koin**: Injeção de dependência.
- **Kotlinx Coroutines**: Operações assíncronas para execução de comandos ADB.
- **ViewModel (Lifecycle)**: Gerenciamento de estado da UI.

## 🚀 Como Executar

Certifique-se de ter o [ADB](https://developer.android.com/studio/releases/platform-tools) instalado e configurado no seu `PATH`.

### Execução via Terminal

1. Clone o repositório.
2. Navegue até a pasta raiz do projeto.
3. Execute o comando correspondente ao seu sistema operacional:

- **macOS/Linux**:
  ```shell
  ./gradlew :composeApp:run
  ```
- **Windows**:
  ```shell
  .\gradlew.bat :composeApp:run
  ```

## 📦 Distribuição

Para gerar os pacotes de instalação nativos (`.dmg`, `.msi`, `.deb`):

```shell
./gradlew :composeApp:package
```

Os arquivos gerados estarão disponíveis em `composeApp/build/compose/binaries`.

---

Desenvolvido por Leandro Carvalho.
