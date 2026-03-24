import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  Stack,
  Typography
} from "@mui/material";
import TrendingUpRoundedIcon from "@mui/icons-material/TrendingUpRounded";
import GroupsRoundedIcon from "@mui/icons-material/GroupsRounded";
import CalendarMonthRoundedIcon from "@mui/icons-material/CalendarMonthRounded";

export function Dashboard() {
  return (
    <Stack spacing={3}>
      {/* Hero card with gradient headline */}
      <Card>
        <CardContent sx={{ p: { xs: 3, sm: 4 } }}>
          <Stack spacing={2}>
            <Typography
              variant="h3"
              component="h1"
              sx={{
                fontWeight: 800,
                lineHeight: 1.15,
                background: "linear-gradient(105deg, #00F0FF 0%, #7C4DFF 42%, #FF453A 100%)",
                WebkitBackgroundClip: "text",
                WebkitTextFillColor: "transparent",
                backgroundClip: "text"
              }}
            >
              Premium Dark SaaS
            </Typography>
            <Typography
              variant="body1"
              color="text.secondary"
              sx={{ maxWidth: (theme) => theme.breakpoints.values.md }}
            >
              Pulpit administracyjny wypożyczalni kamperów — szybki podgląd aktywności, floty i rezerwacji w jednym
              miejscu. Ten blok pokazuje gradientowy nagłówek na ciemnym tle kart.
            </Typography>
            <Box>
              <Button variant="contained" color="primary" size="large">
                Nowa rezerwacja
              </Button>
            </Box>
          </Stack>
        </CardContent>
      </Card>

      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <TrendingUpRoundedIcon color="primary" />
                <Typography variant="subtitle2" color="text.secondary" fontWeight={600}>
                  Przychód (miesiąc)
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                128 400 zł
              </Typography>
              <Typography variant="caption" color="text.secondary">
                +12% vs poprzedni miesiąc
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <GroupsRoundedIcon color="primary" />
                <Typography variant="subtitle2" color="text.secondary" fontWeight={600}>
                  Aktywni klienci
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                342
              </Typography>
              <Typography variant="caption" color="text.secondary">
                28 nowych w tym tygodniu
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={1} mb={1}>
                <CalendarMonthRoundedIcon color="primary" />
                <Typography variant="subtitle2" color="text.secondary" fontWeight={600}>
                  Rezerwacje (dziś)
                </Typography>
              </Stack>
              <Typography variant="h4" fontWeight={700}>
                14
              </Typography>
              <Typography variant="caption" color="text.secondary">
                6 oczekuje na potwierdzenie
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent>
              <Typography variant="h6" fontWeight={700} gutterBottom>
                Flota
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Podgląd dostępnych pojazdów, przeglądów technicznych i statusów — moduł zostanie podłączony do API.
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Card>
            <CardContent>
              <Typography variant="h6" fontWeight={700} gutterBottom>
                Rezerwacje
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Lista bieżących i nadchodzących wynajmów z filtrowaniem po dacie i kliencie.
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Stack>
  );
}
