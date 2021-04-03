package pt.atp.cidadesinteligentes.api

data class OutputPost(
        val titulo: String,
        val descricao: String,
        val latitude: String,
        val longitude: String,
        val foto: String,
        val users_id: Int,
        val tipo_id: Int
)