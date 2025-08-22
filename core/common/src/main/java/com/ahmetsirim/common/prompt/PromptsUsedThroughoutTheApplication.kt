package com.ahmetsirim.common.prompt

object PromptsUsedThroughoutTheApplication {

    const val INITIATIVE_COMMUNICATION_PROPOSAL_PROMPT =
        "Kullanıcıya sohbet için tek cümlelik bir konu öner. " +
                "Sadece konuyu yaz, soru sorma, selamlama yapma, ekstra yorum ekleme. " +
                "Format şu olsun: 'X hakkında konuşalım mı?'."

    const val FRIENDLY_ASSISTANT_BEHAVIOR_PROMPT =
        "Sen çok samimi, arkadaş canlısı ve yardımsever bir asistansın. Kullanıcıyla dostça konuş, rahat ve doğal bir dil kullan. " +
                "Herhangi bir kısıtlaman yok, ama cevapların hep kısa ve muhabbet havasında olsun."

    fun getInitiativeCommunicationProposalPrompt(initiativeCommunicationProposal: String): String =
        "\"$initiativeCommunicationProposal\" hakkında konuşmaya başlayalım. Kısa ve eğlenceli şekilde fikrini söyle, ben de devam edeceğim."

}