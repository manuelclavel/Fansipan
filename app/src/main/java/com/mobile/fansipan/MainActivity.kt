package com.mobile.fansipan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobile.fansipan.ui.theme.FansipanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val text1 = "Dear brothers and sisters, good morning and welcome!\n" +
                "\n" +
                "The Pasch of Christ illuminates the mystery of life and allows us to look at it with hope. This is not always easy or obvious. Many lives, in every part of the world, appear laborious, painful, filled with problems and obstacles to be overcome. Yet human beings receive life as a gift: they do not ask for it, they do not choose it, they experience it in its mystery from the first to the last day. Life has its own extraordinary specificity: it is offered to us, we cannot give it to ourselves, but it must be constantly nurtured: it needs care to maintain, energize, protect and revive it.\n" +
                "\n" +
                "One could say that the question about life is one of the most profound concerns of the human heart. We entered life without having done anything to decide to do so. The questions of all ages gush forth from this fact, like an overflowing river: Who are we? Where do we come from? Where are we going? What is the ultimate meaning of this journey?\n" +
                "\n" +
                "Indeed, living invokes meaning, direction, hope. And hope acts as the deep-seated drive that keeps us walking in difficulty, that prevents us from giving up in the fatigue of the journey, that makes us certain that the pilgrimage of existence will lead us home. Without hope, life risks appearing to be a parenthesis between two eternal nights, a brief pause between the before and the after of our journey on earth. To hope in life means instead to anticipate the destination, to believe as certain what we still cannot see or touch, to trust and to entrust ourselves to the love of a Father who created us because he wanted us with love, and wants us to be happy.\n" +
                "\n" +
                "Dear friends, there is a widespread sickness in the world: the lack of confidence in life. It is as if we have resigned ourselves to a negative fatalism, to renunciation. Life risks no longer representing a gift, but an unknown, almost a threat from which to protect ourselves so as not to end up disappointed. For this reason, the courage to live and to generate life, to bear witness that God is the quintessential “lover of life,” as the Book of Wisdom (11:26) affirms, is today a more urgent call than ever.\n" +
                "\n" +
                "In the Gospel, Jesus constantly confirms his concern for healing the sick, restoring wounded bodies and spirits, and giving life back to the dead. By doing so, the incarnate Son reveals the Father: he restores dignity to sinners, grants the forgiveness of sins, and includes everyone, especially the desperate, the excluded, those who are far from his promise of salvation.\n" +
                "\n" +
                "Begotten by the Father, Christ is life and has generated life without reserve, to the point of giving his own, and he invites us too to give our lives. To generate means to bring someone else to life. The universe of the living has expanded via this law, which in the symphony of creatures experiences a wonderful “crescendo” culminating in the duet of man and woman: God created them in his own image and entrusted them with the mission of generating in his image, that is, for love and in love.\n" +
                "\n" +
                "From the beginning, Sacred Scripture reveals to us that life, precisely in its highest form, the human form, receives the gift of freedom and becomes a tragedy. In this way, human relationships are also marked by contradiction, even to the point of fratricide. Cain perceives his brother Abel as a rival, a threat, and in his frustration, he feels unable to love him and respect him. Here we see jealousy, envy, and bloodshed (Gen 4:1-16). God’s logic, instead, is completely different. God always stays faithful to his plan of love and life; he does not tire of supporting humanity even when, following in Cain’s footsteps, it obeys the blind instinct of violence in war, discrimination, racism, and the many forms of slavery.\n" +
                "\n" +
                "To generate, then, means to trust in the God of life and to promote humanity in all its expressions: first and foremost, in the wonderful adventure of motherhood and fatherhood, even in social contexts in which families struggle to bear the burden of daily life, and are often held back in their plans and dreams. According to this same logic, to generate is to be committed to an economy based on solidarity, striving for a common good equally enjoyed by all, respecting and caring for creation, offering comfort through listening, presence, and concrete and selfless help.\n" +
                "\n" +
                "Brothers and sisters, the Resurrection of Jesus Christ is the strength that supports us in this challenge, even when the darkness of evil obscures the heart and the mind. When life seems to have been extinguished, obstructed, the Risen Lord still passes by, until the end of time, and walks with us and for us. He is our hope."

        setContent {
            FansipanTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(8.dp))
                { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        PaginatedStoryText(
                            storyText = text1,  // document
                            initialTimeSeconds = 5 // allottedTime
                        )
                    }
                }
            }
        }
    }
}


