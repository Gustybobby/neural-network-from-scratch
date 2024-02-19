import Grid from "@/components/tic-tac-toe/grid";

export default function Home() {
  return (
    <main className="w-full min-w-fit min-h-screen flex flex-col items-center space-y-4 bg-white p-4">
      <h1 className="font-extrabold text-7xl text-black text-center">NxN Tic Tac Toe</h1>
      <Grid/>
    </main>
  );
}
