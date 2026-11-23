$ErrorActionPreference = 'Stop'

try {
    $base = $PSScriptRoot
    $credencial = Join-Path $base 'credenciais-sql.dat'
    if (-not (Test-Path -LiteralPath $credencial -PathType Leaf)) {
        throw 'Credencial SQL local não encontrada. Configure o banco conforme o README.'
    }

    $protegida = ConvertTo-SecureString ((Get-Content -LiteralPath $credencial -Raw).Trim())
    $env:TAREFAS_DB_URL = 'jdbc:sqlserver://127.0.0.1:1433;databaseName=GerenciamentoTarefas;encrypt=true;trustServerCertificate=true'
    $env:TAREFAS_DB_USER = 'projetojava'
    $env:TAREFAS_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $protegida).Password

    $chaveGemini = Join-Path $base 'credenciais-gemini.dat'
    if ([string]::IsNullOrWhiteSpace($env:GEMINI_API_KEY) -and (Test-Path -LiteralPath $chaveGemini -PathType Leaf)) {
        $geminiProtegida = ConvertTo-SecureString ((Get-Content -LiteralPath $chaveGemini -Raw).Trim())
        $env:GEMINI_API_KEY = [System.Net.NetworkCredential]::new('', $geminiProtegida).Password
    }
    $env:TAREFAS_AI_MODEL = if ([string]::IsNullOrWhiteSpace($env:GEMINI_API_KEY)) { 'none' } else { 'google-genai' }
    Write-Host 'Iniciando o Gerenciador de Tarefas Web...'
    Write-Host 'Quando aparecer "Tomcat started", abra http://localhost:8080/login'
    & (Join-Path $base 'Projeto\mvnw.cmd') -f (Join-Path $base 'Projeto\pom.xml') spring-boot:run
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao iniciar a aplicação.' }
} catch {
    Write-Host ('Erro: ' + $_.Exception.Message) -ForegroundColor Red
    exit 1
} finally {
    Remove-Item Env:TAREFAS_DB_PASSWORD -ErrorAction SilentlyContinue
    Remove-Item Env:GEMINI_API_KEY -ErrorAction SilentlyContinue
}
