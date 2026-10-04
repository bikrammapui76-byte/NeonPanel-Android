package com.example.neonpanel

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg=Color(0xFF070A0D)
private val Panel=Color(0xFF10151A)
private val Border=Color(0xFF1E2B31)
private val Neon=Color(0xFF39FF88)
private val Muted=Color(0xFF8A969D)

class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent{NeonPanel()}
    }
}

@Composable
fun NeonPanel(){
    val context=LocalContext.current
    val prefs=remember{context.getSharedPreferences("neon_panel",Context.MODE_PRIVATE)}
    var a by rememberSaveable{mutableStateOf(prefs.getBoolean("a",false))}
    var b by rememberSaveable{mutableStateOf(prefs.getBoolean("b",false))}
    var c by rememberSaveable{mutableStateOf(prefs.getBoolean("c",false))}
    fun save(k:String,v:Boolean)=prefs.edit().putBoolean(k,v).apply()
    fun reset(){a=false;b=false;c=false;prefs.edit().clear().apply()}
    Box(Modifier.fillMaxSize().background(Bg).padding(20.dp)){
        Column(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally){
            Spacer(Modifier.height(24.dp))
            Text("NEON PANEL",color=Color.White,fontSize=28.sp,fontWeight=FontWeight.ExtraBold,letterSpacing=2.sp)
            Text("Control Panel",color=Muted,fontSize=13.sp)
            Spacer(Modifier.height(28.dp))
            Feature("Feature A",a){a = !a;save("a",a)}
            Feature("Feature B",b){b = !b;save("b",b)}
            Feature("Feature C",c){c = !c;save("c",c)}
            Spacer(Modifier.height(22.dp))
            Button(onClick={reset()},modifier=Modifier.fillMaxWidth().height(52.dp),
                shape=RoundedCornerShape(14.dp),
                colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF182026),contentColor=Color.White)){
                Text("RESET ALL",fontWeight=FontWeight.Bold,letterSpacing=1.sp)
            }
            Spacer(Modifier.weight(1f))
            Text("NEUTRAL DEMO • LOCAL SETTINGS",color=Color(0xFF536168),fontSize=10.sp,letterSpacing=1.sp)
        }
    }
}

@Composable
fun Feature(title:String,checked:Boolean,onChange:()->Unit){
    val t=rememberInfiniteTransition(label="neon")
    val alpha by t.animateFloat(0.3f,0.85f,infiniteRepeatable(tween(900),RepeatMode.Reverse),label="alpha")
    val border by animateColorAsState(if(checked)Neon else Border,tween(250),label="border")
    Row(Modifier.fillMaxWidth().padding(vertical=7.dp)
        .shadow(if(checked)14.dp,RoundedCornerShape(16.dp),ambientColor=Neon.copy(alpha=alpha),spotColor=Neon.copy(alpha=alpha))
        .background(Panel,RoundedCornerShape(16.dp)).padding(18.dp),
        verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween){
        Column{
            Text(title,color=Color.White,fontSize=15.sp,fontWeight=FontWeight.SemiBold)
            Text(if(checked)"ENABLED" else "DISABLED",color=if(checked)Neon else Muted,fontSize=10.sp,letterSpacing=1.sp)
        }
        Switch(checked=checked,onCheckedChange={onChange()},
            colors=SwitchDefaults.colors(checkedThumbColor=Color.White,checkedTrackColor=Neon.copy(.75f),
                uncheckedThumbColor=Color(0xFF89939A),uncheckedTrackColor=Color(0xFF20282D)))
    }
}
