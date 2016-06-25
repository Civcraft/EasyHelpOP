# EasyHelpOP

Brought to you by the developers from https://www.reddit.com/r/Devoted and https://www.github.com/DevotedMC -- check out our server at play.devotedmc.com

## For Anybody

By default, anyone can use `/helpop`, `/ask`, or `/ho` to ask a question.

Example:

    /helpop Why can't I find any mobs?

## Easy to use for helpers, ops, and admins

Helpers can use `/viewhelp` to see an interactive list of unanswered questions.

Use `/viewhelp all` to see all questions

Use `/viewhelp id [id]` to see a single question by ID

Use `/viewhelp name [player name]` to see a list of all questions asked by a single player

Use `/ignorehelp [id]` to ignore a single question by ID

Use `/replyhelp [id]` to reply to a single question by ID

All lists are represented by an in-game GUI. For console admins, colored lists are used to allow them to participate.

For in-game, to answer a question, just click on it and you will be prompted to provide an answer. Type `cancel` to abandon answering; the question will remain unanswered. 

While one helper is answering, other helpers are prevented from answering (via the `reservationTimeout` configuration). 

## Permissions

If someone is abusing `/helpop`, you can remove their ability to ask questions by removing the `easyhelpop.requesthelp` permission. By default everyone has this permission.

If you have a new helper or OP joining the team, give them permission to answer and view questions by adding
the `easyhelpop.replyhelp` permission. By default, only ops have this permission.

## Advanced configuration

All visible replies are fully configurable, so you can easily tune this plugin to fit the feel and theme of your server. You could also replace them with alternate language prompts, for instance if your server is for a predominantly French speaking crowd.

Check out the example configuration in `/src/resources/config.yml` for more details.

Set `debug:` to true if you want extra console information on what EasyHelpOP is doing.

Set `reservationTimeout:` to the number of milliseconds to "reserve" a question when a helper begins answering it. This is to prevent helpers trying to answer the same question and resulting in player confusion.

The full set of `msg.*` configuration options are as follows:

`unansweredReady:` Prompt sent to a helper upon login if there are unanswered questions pending.

`replyReceived:` Message sent to a player when their question is answered or they log back in later and their question has been answered while they are offline. Use `%HELPER%`, `%QUESTION%` and `%ANSWER%` placeholders. Be sure to at least include `%QUESTION%` and `%ANSWER%` so that the player knows what was answered.

`helperAlert:` Message sent to all online helpers if a player asks a question. Use `%PLAYER%` and `%MESSAGE%` placeholders to show who asked a question and what their question was, respectively.

`questionAdded:` Affirmation sent to player after they ask a question

`questionFailure:` If something went wrong while asking a question, this is sent to the player

`replyAdded:` Affirmation sent to helper after they answer a question

`replyFailure:` If something went wrong while replying, this is sent to the helper

`playerUnknown:` If `/viewhelp [player]` is used but the player can't be found, is the message sent to the helper

`idUnknown:` If `/viewhelp [id]` is used but no message with that ID is found, is the message sent to the helper

`generalFailure:` General catchall for errors, sent to the user whose action caused the unknown error

`replyStart:` Interaction start message while answering a question after selecting it from a `/viewhelp` menu. Use `%QUESTION%` as a placeholder for the text of the question. Might be worth mentioning that typing `cancel` will gracefully exit out of the answering session without sending anything to the player.

`ignoreQuestion:` Message replied to helper if they chose to ignore a question using `/ignorehelp [id]`. This decision is logged.

`reserved:` EasyHelpOp automatically prevents multiple helpers from answering the same question at the same time, to reduce player confusion. This message is sent to a helper trying to answer a question that someone else is answering.

## Database

EasyHelpOP uses a database to track all questions and replies. To configure it, use these settings in the config file:

    db:
      user: "root"
      pass: ""
      hostname: "localhost"
      dbname: "easyhelpop"
      port: 3306

Adjust to fit your server's database. Currently only MySQL/MariaDB are supported.
