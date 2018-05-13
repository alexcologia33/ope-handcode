# Generated by Django 2.0.5 on 2018-05-13 01:53

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0002_disciplina_curso'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='disciplina',
            name='curso',
        ),
        migrations.AddField(
            model_name='disciplina',
            name='cursos',
            field=models.ManyToManyField(blank=True, db_table='CURSO_DISICPLINAS', related_name='disciplinas', to='core.Curso'),
        ),
    ]