import type { ReactNode } from "react";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import {
  AppBar,
  Avatar,
  Box,
  Breadcrumbs,
  Divider,
  IconButton,
  Link as MuiLink,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  Paper,
  ToggleButton,
  ToggleButtonGroup,
  Toolbar,
  Typography
} from "@mui/material";
import DashboardRoundedIcon from "@mui/icons-material/DashboardRounded";
import DirectionsCarFilledRoundedIcon from "@mui/icons-material/DirectionsCarFilledRounded";
import EventNoteRoundedIcon from "@mui/icons-material/EventNoteRounded";
import LogoutRoundedIcon from "@mui/icons-material/LogoutRounded";
import NotificationsRoundedIcon from "@mui/icons-material/NotificationsRounded";
import { Link, Outlet, useNavigate, useRouterState } from "@tanstack/react-router";

import type { AppLanguage } from "../../i18n/config";
import { SUPPORTED_LANGUAGES } from "../../i18n/config";
import { parseAuthRedirectTo } from "../../routes/auth";
import { useAuthStore } from "../../store/auth-store";

const DRAWER_WIDTH = 260;
const SIDEBAR_INNER_BG = "#0D0F17";

const navConfig = [
  { to: "/", key: "layout.nav.dashboard" as const, icon: <DashboardRoundedIcon /> },
  { to: "/fleet", key: "layout.nav.fleet" as const, icon: <DirectionsCarFilledRoundedIcon /> },
  { to: "/reservations", key: "layout.nav.reservations" as const, icon: <EventNoteRoundedIcon /> }
];

const breadcrumbKeyByPath: Record<string, string> = {
  "/": "layout.breadcrumb.dashboard",
  "/auth": "layout.breadcrumb.auth",
  "/fleet": "layout.breadcrumb.fleet",
  "/reservations": "layout.breadcrumb.reservations"
};

type MainLayoutProps = {
  children?: ReactNode;
};

export function MainLayout({ children }: MainLayoutProps) {
  const { t, i18n } = useTranslation();
  const navigate = useNavigate();
  const auth = useAuthStore((state) => state.auth);
  const clearAuth = useAuthStore((state) => state.clearAuth);
  const pathname = useRouterState({ select: (s) => s.location.pathname });
  const currentBreadcrumbKey = breadcrumbKeyByPath[pathname] ?? "layout.breadcrumb.fallback";

  const [userMenuAnchor, setUserMenuAnchor] = useState<null | HTMLElement>(null);
  const userMenuOpen = Boolean(userMenuAnchor);

  const avatarLetter = auth?.email?.charAt(0).toUpperCase() ?? "?";

  const activeLang: AppLanguage = i18n.language.startsWith("pl") ? "pl" : "en";

  function handleUserAvatarClick(event: React.MouseEvent<HTMLElement>) {
    if (auth) {
      setUserMenuAnchor(event.currentTarget);
      return;
    }
    const redirectTo = parseAuthRedirectTo(pathname);
    void navigate({
      to: "/auth",
      ...(redirectTo ? { search: { redirectTo } } : {})
    });
  }

  function handleUserMenuClose() {
    setUserMenuAnchor(null);
  }

  function handleLogout() {
    clearAuth();
    handleUserMenuClose();
    void navigate({ to: "/auth", replace: true });
  }

  return (
    <Box
      sx={{
        minHeight: "100vh",
        bgcolor: "background.default",
        p: 2,
        display: "flex",
        gap: 2,
        alignItems: "stretch"
      }}
    >
      <Paper
        elevation={0}
        sx={{
          width: DRAWER_WIDTH,
          flexShrink: 0,
          borderRadius: "16px",
          overflow: "hidden",
          display: "flex",
          flexDirection: "column",
          bgcolor: SIDEBAR_INNER_BG,
          border: "1px solid rgba(255, 255, 255, 0.08)",
          alignSelf: "stretch"
        }}
      >
        <Toolbar sx={{ px: 2, minHeight: 64 }}>
          <Typography variant="h6" fontWeight={700} noWrap sx={{ color: "text.primary" }}>
            {t("layout.brand")}
          </Typography>
        </Toolbar>
        <Divider sx={{ borderColor: "rgba(255,255,255,0.08)" }} />
        <List sx={{ px: 1, py: 2, flex: 1 }}>
          {navConfig.map((item) => (
            <ListItemButton
              key={item.to}
              component={Link}
              to={item.to}
              selected={pathname === item.to}
              sx={{
                borderRadius: "12px",
                mb: 0.5,
                color: "text.secondary",
                "&.Mui-selected": {
                  bgcolor: "rgba(0, 240, 255, 0.12)",
                  color: "primary.main",
                  "&:hover": { bgcolor: "rgba(0, 240, 255, 0.16)" }
                },
                "&:hover": { bgcolor: "rgba(255, 255, 255, 0.06)" }
              }}
            >
              <ListItemIcon sx={{ color: "inherit", minWidth: 40 }}>{item.icon}</ListItemIcon>
              <ListItemText primaryTypographyProps={{ fontWeight: 600 }} primary={t(item.key)} />
            </ListItemButton>
          ))}
        </List>
      </Paper>

      <Box
        sx={{
          flex: 1,
          minWidth: 0,
          display: "flex",
          flexDirection: "column",
          borderRadius: "16px",
          overflow: "hidden"
        }}
      >
        <AppBar
          position="sticky"
          elevation={0}
          sx={{
            bgcolor: "rgba(9, 10, 15, 0.55)",
            backdropFilter: "blur(10px)",
            WebkitBackdropFilter: "blur(10px)",
            borderBottom: "1px solid rgba(255, 255, 255, 0.08)",
            color: "text.primary"
          }}
        >
          <Toolbar sx={{ justifyContent: "space-between", gap: 2, minHeight: 64 }}>
            <Breadcrumbs aria-label="breadcrumb" sx={{ flex: 1 }} separator="›">
              <MuiLink
                component={Link}
                to="/"
                underline="hover"
                sx={{ color: "text.secondary", fontWeight: 600, "&:hover": { color: "primary.main" } }}
              >
                {t("layout.panel")}
              </MuiLink>
              <Typography color="text.primary" fontWeight={600}>
                {t(currentBreadcrumbKey)}
              </Typography>
            </Breadcrumbs>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
              <ToggleButtonGroup
                value={activeLang}
                exclusive
                size="small"
                aria-label={t("layout.language")}
                onChange={(_, value: AppLanguage | null) => {
                  if (value && SUPPORTED_LANGUAGES.includes(value)) {
                    void i18n.changeLanguage(value);
                  }
                }}
              >
                <ToggleButton value="en">EN</ToggleButton>
                <ToggleButton value="pl">PL</ToggleButton>
              </ToggleButtonGroup>
              <IconButton
                size="large"
                color="inherit"
                aria-label={t("layout.notifications")}
                sx={{ color: "text.secondary" }}
              >
                <NotificationsRoundedIcon />
              </IconButton>
              <IconButton
                onClick={handleUserAvatarClick}
                aria-label={t("layout.userMenu.openMenu")}
                aria-haspopup={auth ? "menu" : undefined}
                aria-expanded={auth ? userMenuOpen : undefined}
                sx={{ p: 0 }}
              >
                <Avatar
                  sx={{
                    width: 40,
                    height: 40,
                    bgcolor: "transparent",
                    color: "primary.main",
                    border: "1px solid rgba(0, 240, 255, 0.35)",
                    fontWeight: 700,
                    cursor: "pointer",
                    opacity: auth ? 1 : 0.65
                  }}
                >
                  {avatarLetter}
                </Avatar>
              </IconButton>
              <Menu
                anchorEl={userMenuAnchor}
                open={userMenuOpen}
                onClose={handleUserMenuClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                transformOrigin={{ vertical: "top", horizontal: "right" }}
                slotProps={{
                  paper: {
                    sx: {
                      mt: 1,
                      minWidth: 220,
                      border: "1px solid rgba(255, 255, 255, 0.08)",
                      bgcolor: "background.paper"
                    }
                  }
                }}
              >
                {auth && (
                  <Box component="div">
                    <Box sx={{ px: 2, py: 1.5, maxWidth: 280 }}>
                      <Typography variant="caption" color="text.secondary" display="block">
                        {t("layout.userMenu.signedInAs")}
                      </Typography>
                      <Typography variant="body2" fontWeight={600} noWrap title={auth.email}>
                        {auth.email}
                      </Typography>
                    </Box>
                    <Divider sx={{ borderColor: "rgba(255,255,255,0.08)" }} />
                    <MenuItem
                      onClick={handleLogout}
                      sx={{ gap: 1, py: 1.25 }}
                    >
                      <ListItemIcon sx={{ minWidth: 36, color: "primary.main" }}>
                        <LogoutRoundedIcon fontSize="small" />
                      </ListItemIcon>
                      {t("auth.logout")}
                    </MenuItem>
                  </Box>
                )}
              </Menu>
            </Box>
          </Toolbar>
        </AppBar>

        <Box
          component="main"
          sx={{
            flex: 1,
            p: { xs: 2, sm: 3 },
            bgcolor: "background.default",
            overflow: "auto"
          }}
        >
          {children ?? <Outlet />}
        </Box>
      </Box>
    </Box>
  );
}
