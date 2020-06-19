# NookBot
![build](https://github.com/gumil/nookbot/workflows/build/badge.svg)
[![telegram](https://img.shields.io/badge/telegram-bot-blue)](https://telegram.me/Nookex_bot)
[![detekt](https://img.shields.io/badge/code%20quality-detekt-red)](config/detekt/detekt.yml)
[![codecov](https://codecov.io/gh/gumil/NookBot/branch/master/graph/badge.svg)](https://codecov.io/gh/gumil/NookBot)

Telegram bot to manage order exchanges between members in a group

## Commands
 - /order - create an order that other members can take and deliver it to you.
 - /sent - marks order taken as delivered.
 - /list - lists all pending orders.

## Overview
![](img/high-level-design.png)

The bot uses a Kotlin stack:
 - [Kotless](https://github.com/JetBrains/kotless/) - serverless framework
 - [DynamoDB](https://aws.amazon.com/dynamodb/) - persistence
 - [Kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - json serialization
 - [Ktor-client](https://ktor.io/clients/index.html) - http requests