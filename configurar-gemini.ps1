$ErrorActionPreference = 'Stop'

try {
    $destino = Join-Path $PSScriptRoot 'credenciais-gemini.dat'
    Write-Host 'Cole a chave da API Gemini. Ela será protegida pela sua conta Windows.'
    $chave = Read-Host 'Chave Gemini' -AsSecureString
    if ($chave.Length -eq 0) { throw 'A chave não pode ficar vazia.' }
    $chave | ConvertFrom-SecureString | Set-Content -LiteralPath $destino -Encoding UTF8
    Write-Host 'Chave salva localmente. Agora abra iniciar-web.cmd para usar o sistema.' -ForegroundColor Green
} catch {
    Write-Host ('Erro: ' + $_.Exception.Message) -ForegroundColor Red
    exit 1
}
