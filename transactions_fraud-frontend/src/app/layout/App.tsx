
import { Outlet } from "react-router-dom";
import { ConfigProvider, theme as antdTheme } from "antd";
import { useContext } from "react";
import { ThemeContext, ThemeProvider } from "../context/ThemeContex";


function AppContent() {
  const { theme } = useContext(ThemeContext);

  return (
    <ConfigProvider
      key={theme}
      theme={{
        algorithm: theme === "dark" ? antdTheme.darkAlgorithm : antdTheme.defaultAlgorithm,
      }}
    >
      <Outlet />
    </ConfigProvider>
  );
}

export default function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}
