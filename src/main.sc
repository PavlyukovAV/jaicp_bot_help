import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.dialogflow.DialogflowAgentContext
import com.justai.jaicf.channel.jaicp.JaicpPollingConnector
import com.justai.jaicf.channel.jaicp.channels.ChatWidgetChannel
import com.justai.jaicf.channel.jaicp.channels.TelephonyChannel
import com.justai.jaicf.channel.jaicp.dto.TelephonyDto
import com.justai.jaicf.reactions.jaicp.JaicpReactions

fun main() {
    val helloState = "/hello"
    val weatherState = "/weather"
    val currencyState = "/currency"
    val noMatchState = "/NoMatch"

    val bot = BotEngine(
        model = HelloBotModel,
        defaultContext = { DialogflowAgentContext },
        activators = listOf(DialogflowAgentContext.model),
        states = listOf(
            helloState,
            weatherState,
            currencyState,
            noMatchState
        ),
        reactions = JaicpReactions
    )

    JaicpPollingConnector(
        bot = bot,
        accessToken = "YOUR_JAICP_PROJECT_ACCESS_TOKEN",
        channels = listOf(ChatWidgetChannel, TelephonyChannel),
        telephonyMapper = { TelephonyDto(it) }
    ).runBlocking()
}

object HelloBotModel : BotEngine by BotEngine(
    model = {
        state(helloState) {
            activators {
                event("/start")
                intent("HelloIntent")
            }
            action {
                reactions.say("Привет! Чем я могу помочь тебе сегодня?")
                reactions.buttons("Погода", "Курс валют")
            }
        }

        state(weatherState) {
            activators {
                intent("WeatherIntent")
            }
            action {
                reactions.say("Погода сегодня будет солнечной.")
            }
        }

        state(currencyState) {
            activators {
                intent("CurrencyIntent")
            }
            action {
                reactions.say("Курс доллара к рублю сегодня составляет 75 рублей.")
            }
        }

        state(noMatchState) {
            activators {
                catchAll()
            }
            action {
                reactions.say("Извините, я не понял ваш запрос. Пожалуйста, повторите.")
            }
        }
    },
    defaultContext = { DialogflowAgentContext },
    activators = listOf(DialogflowAgentContext.model),
    states = listOf(
        helloState,
        weatherState,
        currencyState,
        noMatchState
    ),
    reactions = JaicpReactions
)
