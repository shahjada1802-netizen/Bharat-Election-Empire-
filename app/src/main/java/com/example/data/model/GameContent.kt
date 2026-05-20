package com.example.data.model

import com.example.data.entity.StateProgress

data class StateInitialInfo(
    val name: String,
    val assemblySeats: Int,
    val lokSabhaSeats: Int,
    val ruralPercent: Int,
    val urbanPercent: Int,
    val mainIssue: String,
    val baseSupportPlayer: Double,
    val baseSupportOpposition: Double,
    val standardAtmosphere: String
)

object GameContent {
    val STATES_DATA = listOf(
        StateInitialInfo("Uttar Pradesh", 403, 80, 77, 23, "Agriculture", 12.0, 50.0, "Agrarian stress in sugar belt"),
        StateInitialInfo("Maharashtra", 288, 48, 55, 45, "Technology", 10.0, 52.0, "Corporate economic boom"),
        StateInitialInfo("West Bengal", 294, 42, 68, 32, "Welfare", 8.0, 54.0, "High festive season"),
        StateInitialInfo("Bihar", 243, 40, 89, 11, "Unemployment", 15.0, 48.0, "Youth migration protests"),
        StateInitialInfo("Tamil Nadu", 234, 39, 52, 48, "Education", 7.0, 55.0, "Language and local pride debates"),
        StateInitialInfo("Karnataka", 224, 28, 62, 38, "Technology", 11.0, 50.0, "Tech start-up cluster expansion"),
        StateInitialInfo("Madhya Pradesh", 230, 29, 73, 27, "Agriculture", 10.0, 53.0, "Water shortage alerts"),
        StateInitialInfo("Gujarat", 182, 26, 57, 43, "Infrastructure", 14.0, 52.0, "Industrial corridor inaugurations"),
        StateInitialInfo("Rajasthan", 200, 25, 76, 24, "Agriculture", 9.0, 51.0, "Drinking water canal demands"),
        StateInitialInfo("Andhra Pradesh", 175, 25, 70, 30, "Welfare", 8.0, 54.0, "Subsidies allocation disputes"),
        StateInitialInfo("Kerala", 140, 20, 53, 47, "Education", 6.0, 56.0, "Healthcare system praise"),
        StateInitialInfo("Delhi", 70, 7, 5, 95, "Unemployment", 13.0, 47.0, "Air pollution dynamic protests")
    )

    fun initializeStates(): List<StateProgress> {
        val weatherOptions = listOf("Sunny", "Monsoon", "Heatwave", "Cyclone")
        return STATES_DATA.mapIndexed { idx, info ->
            StateProgress(
                stateName = info.name,
                assemblySeats = info.assemblySeats,
                lokSabhaSeats = info.lokSabhaSeats,
                ruralPercent = info.ruralPercent,
                urbanPercent = info.urbanPercent,
                mainIssue = info.mainIssue,
                playerSupportRate = info.baseSupportPlayer,
                oppositionSupportRate = info.baseSupportOpposition,
                undecidedSupportRate = 100.0 - info.baseSupportPlayer - info.baseSupportOpposition,
                ralliesHeld = 0,
                advertisingSpent = 0.0,
                welfareSchemesPromised = 0,
                weather = weatherOptions[idx % weatherOptions.size],
                regionalAtmosphere = info.standardAtmosphere
            )
        }
    }

    data class AssemblyQuestion(
        val question: String,
        val options: List<DebateOption>
    )

    data class DebateOption(
        val text: String,
        val summary: String,
        val fundsEffect: Double, // Lakhs (+ or -)
        val popularityEffect: Int, // Fame (+ or -)
        val supportEffect: Double, // % support rate change
        val scandalEffect: Int, // Scandal change
        val responseDialog: String
    )

    val TV_DEBATE_QUESTIONS = listOf(
        AssemblyQuestion(
            question = "Rival parties allege that your manifesto lacks concrete solutions for agricultural distress in rural regions. How do you respond?",
            options = listOf(
                DebateOption(
                    text = "Promise high MSP & loan waivers",
                    summary = "Focus aggressively on farmers' direct welfare payouts. Wins rural votes heavily but drains your campaign treasury.",
                    fundsEffect = -15.0,
                    popularityEffect = 12,
                    supportEffect = 6.5,
                    scandalEffect = 0,
                    responseDialog = "Farmers are the backbone of India! We promise direct minimum support price (MSP) guarantees and interest-free loans! The crowd roars in approval!"
                ),
                DebateOption(
                    text = "Propose tech-driven dryland farming support",
                    summary = "Champion cold storage infrastructure and drone subsidies. Appealing to educated and startup blocks.",
                    fundsEffect = -5.0,
                    popularityEffect = 8,
                    supportEffect = 4.0,
                    scandalEffect = 0,
                    responseDialog = "We need modern solutions. Our tech-driven irrigation schemes and drone subsidies will double yields without leaking funds to intermediaries. Policy experts praise your vision!"
                ),
                DebateOption(
                    text = "Accuse rivals of deep historical corruption",
                    summary = "Divert attention by throwing a counter-political slap. Aggressive, raises popularity, high risk.",
                    fundsEffect = 0.0,
                    popularityEffect = 15,
                    supportEffect = 2.0,
                    scandalEffect = 5,
                    responseDialog = "Those who plundered India for seventy years should not preach to us! Their own storage warehouses are empty due to scams! The media goes wild over the political clash!"
                )
            )
        ),
        AssemblyQuestion(
            question = "How will your party tackle the rising urban youth unemployment crisis in India's technology hubs?",
            options = listOf(
                DebateOption(
                    text = "Launch an Unemployed Youth stipend",
                    summary = "Direct monthly allowances for graduates. Instant massive popularity, extremely high budget cost.",
                    fundsEffect = -25.0,
                    popularityEffect = 18,
                    supportEffect = 8.0,
                    scandalEffect = 0,
                    responseDialog = "No youth should feel abandoned. We will provide a ₹3,000 monthly startup-seeking stipend to all graduates! Youth influencers launch trend posts about you!"
                ),
                DebateOption(
                    text = "Develop regional startup incubation centers",
                    summary = "Provide tax breaks to corporations hiring freshers. Balanced, low cost, builds tech index.",
                    fundsEffect = -8.0,
                    popularityEffect = 10,
                    supportEffect = 5.0,
                    scandalEffect = 0,
                    responseDialog = "Instead of freebies, we build futures. We will establish 100 startup incubation clusters and offer 3-year tax holidays for green-tech businesses. Tech founders endorse your policy!"
                ),
                DebateOption(
                    text = "Evade and shift the debate to national identity",
                    summary = "Deflect using cultural rallying cries. Zero cost, works well with nationalist sentiment.",
                    fundsEffect = 0.0,
                    popularityEffect = 7,
                    supportEffect = 2.5,
                    scandalEffect = -5,
                    responseDialog = "Our youth need patriotism and self-reliance, not just state dependency! We will restore historical pride. Supporters cheer passionately."
                )
            )
        ),
        AssemblyQuestion(
            question = "A sting operation claims of secret funds and dynamic kickbacks inside your high-level campaign committee. What is your defence?",
            options = listOf(
                DebateOption(
                    text = "Order a fully transparent judicial inquiry",
                    summary = "Proactive and ethical. Reduces scandal dramatically, costs minor funds to manage media.",
                    fundsEffect = -3.0,
                    popularityEffect = 5,
                    supportEffect = 1.5,
                    scandalEffect = -25,
                    responseDialog = "We maintain absolute absolute clean governance. I am immediately appointing a retired Supreme Court judge to investigate this within 15 days! The public respects your quick ethical pivot."
                ),
                DebateOption(
                    text = "Claim it as a deepfake fabricated by rival cyber-cells",
                    summary = "Aggressive media denial. Splits opinion, raises support among base, medium risk.",
                    fundsEffect = -1.0,
                    popularityEffect = 9,
                    supportEffect = 3.0,
                    scandalEffect = -10,
                    responseDialog = "This is a synthetic video engineered in foreign servers by our nervous opponents! Our technical cell has already filed legal notices. Your core supporters rally behind you!"
                ),
                DebateOption(
                    text = "Host a high-power stadium speech to override the news",
                    summary = "High visual spectacle. Focuses on massive display of support to drown the negative clips.",
                    fundsEffect = -10.0,
                    popularityEffect = 14,
                    supportEffect = 4.5,
                    scandalEffect = 2,
                    responseDialog = "Let them run animations! The massive ocean of humanity assembled here in this stadium is my only witness and jury! The visual crowd presence distracts from the news cycle!"
                )
            )
        ),
        AssemblyQuestion(
            question = "Air pollution and urban waste have made metropolitan cities unlivable. What is your environmental charter?",
            options = listOf(
                DebateOption(
                    text = "Impose strict vehicle pollution green tags",
                    summary = "Strong policy enforcement. Wins urban votes but causes temporary fuel price protest scares.",
                    fundsEffect = -4.0,
                    popularityEffect = 6,
                    supportEffect = 3.5,
                    scandalEffect = 1,
                    responseDialog = "Clean air is a fundamental right. We will replace old bus fleets with electric vehicles and mandate pollution-control geo-fences. Environmentalists applaud."
                ),
                DebateOption(
                    text = "Offer subsidy grants to solar conversions",
                    summary = "Subsidize rooftop clean solar installations. Wins urban middle class fully.",
                    fundsEffect = -12.0,
                    popularityEffect = 11,
                    supportEffect = 5.0,
                    scandalEffect = 0,
                    responseDialog = "We will offer a 50% capital subsidy for standard domestic solar plants. This is sustainable growth! Slogans about clean solar trend in tech capitals."
                ),
                DebateOption(
                    text = "Divert blame to regional stubble burning other states",
                    summary = "Easy scapegoat pivot. Zero cost, wins statewide defense pride.",
                    fundsEffect = 0.0,
                    popularityEffect = 4,
                    supportEffect = 1.0,
                    scandalEffect = 0,
                    responseDialog = "Our city is clean! The smoke travels from other states where our opponents run inefficient agricultural protocols. We are the victims here! Local pride flares up."
                )
            )
        )
    )

    data class CrisisScenario(
        val title: String,
        val description: String,
        val options: List<CrisisOption>
    )

    data class CrisisOption(
        val text: String,
        val effectText: String,
        val fundsAction: Double,
        val approvalAction: Int,
        val scandalAction: Int,
        val resultLog: String
    )

    val GOVT_CRISES = listOf(
        CrisisScenario(
            title = "Unseasonal Cyclone Hits Coastline",
            description = "A massive category-4 cyclone has ripped through coastal farmlands and damaged power infrastructure. High casualty risk and crop losses are reported.",
            options = listOf(
                CrisisOption(
                    text = "Deploy immediate ₹50 Crore emergency package",
                    effectText = "High cost, massive approval boost, clears accountability.",
                    fundsAction = -50.0,
                    approvalAction = 20,
                    scandalAction = -10,
                    resultLog = "Your government responded with lightning speed! Cargo planes dropped food packets and state utility teams restored electric grids within 48 hours. Approval rating spikes!"
                ),
                CrisisOption(
                    text = "Deploy NDRF personnel and hold press briefings",
                    effectText = "Low cost, moderate approval growth, stable.",
                    fundsAction = -10.0,
                    approvalAction = 8,
                    scandalAction = 0,
                    resultLog = "Rescue operations are handled efficiently by brave NDRF officers. Your press briefings reassure local communities, though rehabilitation funds are delayed."
                ),
                CrisisOption(
                    text = "Request international relief loans",
                    effectText = "Gain short-term cash but invite political sarcasm about dependency.",
                    fundsAction = 25.0,
                    approvalAction = -5,
                    scandalAction = 15,
                    resultLog = "Receiving foreign rehabilitation funding makes the treasury happy, but opponents mock your inability to manage domestic emergencies independently."
                )
            )
        ),
        CrisisScenario(
            title = "Inflation Spikes Due to Fuel Price Surge",
            description = "Middle-class families are protesting on streets across India as fuel prices touch record highs, triggering secondary commodity inflation.",
            options = listOf(
                CrisisOption(
                    text = "Slash statewide VAT tax fuel rates",
                    effectText = "Drastic loss in monthly revenues but massive public delight.",
                    fundsAction = -30.0,
                    approvalAction = 18,
                    scandalAction = -5,
                    resultLog = "By reducing regional taxes, fuel drops ₹8 per litre! The public celebrates this historic relief, though development capital is squeezed."
                ),
                CrisisOption(
                    text = "Promote electric conversion subsidies",
                    effectText = "Medium budget expenditure, builds long-term green economy.",
                    fundsAction = -15.0,
                    approvalAction = 10,
                    scandalAction = 0,
                    resultLog = "You announce heavy cash incentives for electric scooters and cargo rickshaws. Forward-thinking urban communities appreciate the strategic vision."
                ),
                CrisisOption(
                    text = "Ban opposition protests using special ordinances",
                    effectText = "Severe civil liberty backlash! Deep scandal surge, zero budget cost.",
                    fundsAction = 0.0,
                    approvalAction = -20,
                    scandalAction = 40,
                    resultLog = "Enforcing Section 144 to quieten protests creates a massive controversy in the national media! Editorial columns compare your government to an autocracy."
                )
            )
        )
    )
}
