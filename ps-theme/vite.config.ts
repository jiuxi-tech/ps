import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { keycloakify } from "keycloakify/vite-plugin";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),
        keycloakify({
            accountThemeImplementation: "none",
            // 统一主题名称为 ps-theme，便于在 Keycloak 中选择并匹配挂载的 JAR
            themeName: "ps-theme",
            extraThemeProperties: [
                "parent=keycloak",
                
            ]
        })
    ]
});
