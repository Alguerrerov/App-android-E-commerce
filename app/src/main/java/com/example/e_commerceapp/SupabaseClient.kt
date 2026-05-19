package com.example.e_commerceapp

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://klbbrgpaibbgaayaemwn.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtsYmJyZ3BhaWJiZ2FheWFlbXduIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzkxMjM1NzksImV4cCI6MjA5NDY5OTU3OX0.PXaUvuNT1TI6MPBzRzHf4HRluRzSKX9AggdgyGq587g"
    ) {
        install(Postgrest)
        install(Auth)
    }
}